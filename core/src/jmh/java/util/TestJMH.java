package util;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-Xms10G", "-Xmx10G"})
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
public class TestJMH
{
	private static Random random = new Random();
	
	int N = 10_000_000;
	private List<Car> list;
	private Car[] array;
	
	private class Car
	{
		private final int size;
		
		public Car()
		{
			this.size = random.nextInt(100);
		}
		
		public int getSize()
		{
			return size;
		}
	}
	
    @Setup
    public void setup()
    {
    	list = new LinkedList<>();   	
    	for(int i = 0; i < N*10; i++) list.add(new Car());   	
    	for(int i = 0; i < N*9; i++)  list.remove(0);
    	
    	array = new Car[N];
    	for(int i = 0; i < N; i++) array[i] = new Car();
    	
    	System.out.println(N+" "+list.size()+" "+array.length);
    }
	
    public static void main(String[] args) throws RunnerException
    {
        Options opt = new OptionsBuilder()
                .include(TestJMH.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
    
    @Benchmark
    public void testList(Blackhole bh)
    {
    	long sum = 0;
    	
        for(Car car : list)
        {
        	sum += car.getSize();
        }
        
        bh.consume(sum);
    }
    
    @Benchmark
    public void testArray(Blackhole bh)
    {
    	long sum = 0;
    	
        for(Car car : array)
        {
        	sum += car.getSize();
        }
        
        bh.consume(sum);
    }
}
