package com.moag.game.utils;

public class Timer
{
	private static long lastFrameTime = getTime();
	private static long lastTickTime = getTime();
	
	private static int frames;
	private static int ticks;
	
	private static int FPS = 60; 
	private static int TPS;
	
	private static float logicFrequency;
	private static float logicDeltaTime; // ms for each logic update
	
	private static long timeleft;
	private static byte cpuUsage;
	
    public static void updateFPS()
    {
        if(getTime() - lastFrameTime > 1000)
        {     	
        	FPS = frames;
        	frames = 0;
            lastFrameTime += 1000;
        }
        frames++;
    }
    
    public static void updateTPS(long timeLeft)
    {
    	timeleft += timeLeft;
    	
        if(getTime() - lastTickTime > 1000)
        {     	
        	TPS = ticks;
        	ticks = 0;
            lastTickTime += 1000;
            
            cpuUsage = Clamp.clampByte((byte)(100 - (timeleft/10f)), (byte)1, (byte)100);
            
            timeleft = 0;
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
    
    /** Return the amount of FPS */
    public static int getFPS()
    {
    	return FPS;
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
    
    public static int getCPUUsage()
    {
    	return cpuUsage;
    }
}