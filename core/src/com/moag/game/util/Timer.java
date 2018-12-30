package com.moag.game.util;

public class Timer
{
	private static long lastTickTime = getTime();
	
	private static int ticks;
	
	private static int TPS;
	
	private static float logicFrequency;
	private static float logicDeltaTime; // ms for each logic update
	
    public static void updateTPS()
    {
        if(getTime() - lastTickTime > 1000)
        {     	
        	TPS = ticks;
        	ticks = 0;
            lastTickTime += 1000;
        }
        ticks++;      
    }
	
    public static long getTime()
    {
        return System.currentTimeMillis();
    }   
    
    public static long getNanoTime()
    {
        return System.nanoTime();
    }

    public static int getTPS()
    {
    	return TPS;
    }
    
	public static void setLogicFrequency(float frequency)
	{
		logicFrequency = frequency;
		logicDeltaTime = 1000f / logicFrequency;
	}
	
    public static float getLogicFrequency()
    {
    	return logicFrequency;
    }
    
    public static float getLogicDeltaTime()
    {
    	return logicDeltaTime;
    }
}