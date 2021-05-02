# MulDiJson

### CLI Arguments

###### JMH

```shell
// The number of processes to be tested in JMH.
-jf, --jmh_forks 

// The number of warmup iterations in JMH.
-jwi, --jmh_warmup_iterations

// The number of measurement iterations in JMH.
-jmi, --jmh_measurement_iterations

// The duration of measurement in JMH.
-jmt, --jmh_measurement_time

// The number of testing threads in JMH.
-jt, --jmh_threads
```



###### `java.lang.Runtime`

```shell
// The number of processes to be tested in memory testing.
-rf, --runtime_forks

// The number of measurement iterations in memory testing.
-rmi, --runtime_measurement_iterations
```



###### Others

```shell
// A csv path that contains the behaviors to be tested.
-i, --includes

// The mode of generating input.
-m, --input_mode
// Options of input mode: 
// [CARRIAGE, USERS, CLIENTS, FIELDS, MUTATED_CARRIAGE, MUTATED_USERS, MUTATED_CLIENTS, MUTATED_CARRIAGE]

// The name of result csv file. 
// The result of time measurement will be put in '/result/time/' directory,
// the result of memory measurement will be put in '/result/memory/' directory,
// and the result of differential testing will be put in '/result/diff/' directory.
// The mutated .java file will be put in 'result/class/' directory.
-o, --output_name

// The times of applying mutations.
-mt, --mutation_times
```



**Because `LoganSquare` needs to generate a mapper `.class` file before parsing, while mutated classes are loaded at runtime,  there is no such mapper `.class` file for mutated class. Therefore, here we exclude the measurement of `LoganSquare` on mutated  classes.**