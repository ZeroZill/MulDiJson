package org.zerozill.muldijson.validation;

import com.bluelinelabs.logansquare.NoSuchMapperException;
import org.zerozill.muldijson.factory.ParserFactory;
import org.zerozill.muldijson.parser.AbstractJsonParser;
import org.zerozill.muldijson.parser.ParserClassification;
import org.zerozill.muldijson.parser.Parsers;
import org.zerozill.muldijson.util.CompareUtil;

import java.security.InvalidParameterException;
import java.util.*;

public class CorrectnessValidation {
    private Map<Parsers, AbstractJsonParser> parsers;

    private Map<Parsers, AbstractJsonParser> beanTypeParsers;
    private Map<Parsers, ParserValidator> beanTypeValidatorMap;

    private Map<Parsers, AbstractJsonParser> nonBeanTypeParsers;
    private Map<Parsers, ParserValidator> nonBeanTypeValidatorMap;

    public CorrectnessValidation() {
        initParsers();
    }

    private void initParsers() {
        ParserFactory factory = new ParserFactory();
        parsers = new HashMap<>();
        beanTypeValidatorMap = new HashMap<>();
        beanTypeParsers = new HashMap<>();
        nonBeanTypeValidatorMap = new HashMap<>();
        nonBeanTypeParsers = new HashMap<>();

        Parsers[] parserTypes = Parsers.values();
        for (Parsers parserType : parserTypes) {
            AbstractJsonParser jsonParser = factory.getParser(parserType);
            parsers.put(parserType, jsonParser);

            if (ParserClassification.isType(parserType, ParserClassification.BEAN_TYPE)) {
                beanTypeParsers.put(parserType, jsonParser);
                beanTypeValidatorMap.put(parserType, new BeanTypeParserValidator(jsonParser));
            }

            if (ParserClassification.isType(parserType, ParserClassification.NON_BEAN_TYPE)) {
                nonBeanTypeParsers.put(parserType, jsonParser);
                nonBeanTypeValidatorMap.put(parserType, new NonBeanTypeParserValidator(jsonParser));
            }
        }
    }

    private <T> ValidationGraph initGraph(int parserClassification, boolean fromJson, Object param, Class<T> clazz) {
        ValidationGraph graph;
        Set<Parsers> parsersOfType;

        switch (parserClassification) {
            case ParserClassification.BEAN_TYPE:
                graph = new ValidationGraph(beanTypeParsers);
                parsersOfType = ParserClassification.BeanTypeParsers;
                break;
            case ParserClassification.NON_BEAN_TYPE:
                graph = new ValidationGraph(nonBeanTypeParsers);
                parsersOfType = ParserClassification.NonBeanTypeParsers;
                break;
            default:
                throw new InvalidParameterException("Invalid parser classification number.");
        }

        Parsers[] parsersOfTypeArray = parsersOfType.toArray(new Parsers[0]);
        // compare one by one
        for (int i = 0; i < parsersOfTypeArray.length; i++) {
            for (int j = i + 1; j < parsersOfTypeArray.length; j++) {
                try {
                    Parsers oneParserType = parsersOfTypeArray[i];
                    Parsers anotherParserType = parsersOfTypeArray[j];
                    AbstractJsonParser oneParser = parsers.get(oneParserType);
                    AbstractJsonParser anotherParser = parsers.get(anotherParserType);
                    ParserValidator validator;
                    ValidationResult result;

                    switch (parserClassification) {
                        case ParserClassification.BEAN_TYPE:
                            validator = beanTypeValidatorMap.get(oneParserType);
                            if (fromJson) {
                                String json = (String) param;
                                T bean = oneParser.deserialize(json, clazz);
                                T targetBean = anotherParser.deserialize(json, clazz);
                                result = validator.isSameDeserializationResult(bean, targetBean);
                            } else {
                                if (!clazz.isInstance(param)) {
                                    throw new InvalidParameterException("The input bean object should be an instance of clazz.");
                                }

                                String json = oneParser.serialize(param);
                                String targetJson = anotherParser.serialize(param);
                                result = validator.isSameSerializationResult(json, targetJson);
                            }
                            break;
                        case ParserClassification.NON_BEAN_TYPE:
                            validator = nonBeanTypeValidatorMap.get(oneParserType);
                            if (fromJson) {
                                String json = (String) param;
                                Object jsonObject = oneParser.parse(json);
                                Object targetJsonObject = anotherParser.parse(json);
                                result = validator.isSameDeserializationResult(jsonObject, targetJsonObject);
                            } else {
                                throw new InvalidParameterException(
                                        "Cannot compare the results of libraries defined JsonObject to Json String."
                                );
                            }
                            break;
                        default:
                            throw new InvalidParameterException("Invalid parser classification number.");
                    }

                    if (result.isSame()) {
                        graph.addEdge(oneParserType, anotherParserType);
                    } else {
                        graph.addEdge(oneParserType, anotherParserType, result.isSame(), result.getDetail());
                    }

                } catch (NoSuchMapperException e) {
                    System.err.println("[Correctness Validation]Logan Square does not support runtime loaded class, here we skip it.");
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }
        return graph;
    }

    public <T> String validateBeanToJson(Object bean, Class<T> clazz) {
        ValidationGraph graph = initGraph(ParserClassification.BEAN_TYPE, false, bean, clazz);
        return graph.calcAndGetDifferences();
    }

    public <T> String validateJsonToBean(String json, Class<T> clazz) {
        ValidationGraph graph = initGraph(ParserClassification.BEAN_TYPE, true, json, clazz);
        return graph.calcAndGetDifferences();
    }

    public String validateJsonToObject(String json) {
        ValidationGraph graph = initGraph(ParserClassification.NON_BEAN_TYPE, true, json, null);
        return graph.calcAndGetDifferences();
    }

}

class ValidationGraph {
    private Map<Parsers, ValidationNode> nodes;
    private List<ValidationNode> connections;

    public ValidationGraph(int parserClassification) {
        nodes = new HashMap<>();
        connections = new ArrayList<>();
        Set<Parsers> parsers;

        switch (parserClassification) {
            case ParserClassification.BEAN_TYPE:
                parsers = ParserClassification.BeanTypeParsers;
                break;
            case ParserClassification.NON_BEAN_TYPE:
                parsers = ParserClassification.NonBeanTypeParsers;
                break;
            default:
                throw new InvalidParameterException("Invalid parser classification number.");
        }

        for (Parsers parser : parsers) {
            nodes.put(parser, new ValidationNode(parser));
        }
    }

    public ValidationGraph(Map<Parsers, AbstractJsonParser> parsersMap) {
        nodes = new HashMap<>();
        connections = new ArrayList<>();

        for (Parsers parser : parsersMap.keySet()) {
            nodes.put(parser, new ValidationNode(parser));
        }
    }

    public void addEdge(Parsers oneNode, Parsers anotherNode) {
        addEdge(oneNode, anotherNode, true, null);
    }

    public void addEdge(Parsers oneNode, Parsers anotherNode, boolean isSame, String detail) {
        if (!nodes.get(oneNode).containsEdge(anotherNode)) {
            nodes.get(oneNode).addEdge(oneNode, anotherNode, isSame, detail);
            nodes.get(anotherNode).addEdge(anotherNode, oneNode, isSame, detail);
        }
    }

    private void calcConnectedGraph() {
        if (nodes.size() == 0) {
            return;
        }

        Set<Parsers> visited = new HashSet<>();
        Queue<Parsers> queue = new LinkedList<>();

        for (Parsers p : nodes.keySet()) {
            if (visited.contains(p)) {
                continue;
            }

            ValidationNode node = nodes.get(p);
            connections.add(node);
            visited.add(p);
            queue.add(p);
            while (!queue.isEmpty()) {
                Parsers temp = queue.poll();
                ValidationNode tempNode = nodes.get(temp);

                for (ValidationEdge e : tempNode.neighbors) {
                    if (!visited.contains(e.anotherNode) && e.isSame) {
                        visited.add(e.anotherNode);
                        queue.add(e.anotherNode);
                    }
                }
            }
        }
    }

    public List<ValidationNode> getConnections() {
        return connections;
    }

    public List<ValidationNode> calcAndGetConnections() {
        calcConnectedGraph();
        return connections;
    }

    public String getDifferences() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < connections.size(); i++) {
            for (int j = i + 1; j < connections.size(); j++) {
                ValidationNode nodeI = connections.get(i);
                ValidationNode nodeJ = connections.get(j);
                Parsers parserTypeI = nodeI.parser.parserType;
                Parsers parserTypeJ = nodeJ.parser.parserType;
                for (ValidationEdge e : nodeI.neighbors) {
                    if (e.oneNode == parserTypeI && e.anotherNode == parserTypeJ && !e.isSame) {
                        sb.append(CompareUtil.separateLine1)
                                .append("Difference between ")
                                .append(parserTypeI)
                                .append(" and ")
                                .append(parserTypeJ)
                                .append("\n")
                                .append(e.detail)
                                .append("\n")
                                .append(CompareUtil.separateLine1)
                                .append("\n");
                    }
                }
            }
        }
        return sb.length() == 0 ?
                CompareUtil.separateLine1 + "There is no differences among all the results.\n" + CompareUtil.separateLine1
                :
                sb.toString();
    }

    public String calcAndGetDifferences() {
        calcConnectedGraph();
        return getDifferences();
    }
}

class ValidationNode {
    public AbstractJsonParser parser;
    public List<ValidationEdge> neighbors;

    public ValidationNode(AbstractJsonParser parser) {
        this.parser = parser;
        neighbors = new LinkedList<>();
    }

    public ValidationNode(Parsers parserType) {
        this.parser = new ParserFactory().getParser(parserType);
        neighbors = new LinkedList<>();
    }

    public void addEdge(Parsers oneNode, Parsers anotherNode, boolean isSame, String detail) {
        neighbors.add(new ValidationEdge(oneNode, anotherNode, isSame, detail));
    }

    public boolean containsEdge(Parsers parser) {
        for (ValidationEdge edge : neighbors) {
            if (edge.anotherNode == parser) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Parser : ").append(parser.parserType).append("\nNeighbors : ");
        for (ValidationEdge e : neighbors) {
            sb.append(e);
        }
        return sb.toString();
    }
}

class ValidationEdge {
    public Parsers oneNode;
    public Parsers anotherNode;
    public boolean isSame;
    public String detail;

    public ValidationEdge(Parsers oneNode, Parsers anotherNode) {
        this.oneNode = oneNode;
        this.anotherNode = anotherNode;
        this.isSame = true;
        this.detail = null;
    }

    public ValidationEdge(Parsers oneNode, Parsers anotherNode, boolean isSame, String detail) {
        this.oneNode = oneNode;
        this.anotherNode = anotherNode;
        this.isSame = isSame;
        this.detail = detail;
    }

    @Override
    public String toString() {
        return String.format("From %s to %s, same result: %s\n", oneNode, anotherNode, isSame);
    }
}
