package util;

public class Clamp
{
    public static byte clampByte(byte value, byte min, byte max)
    {
        return value < min ? min : (value > max ? max : value);
    }
	
    public static short clampShort(short value, short min, short max)
    {
        return value < min ? min : (value > max ? max : value);
    }
    
    public static int clampInt(int value, int min, int max)
    {
        return value < min ? min : (value > max ? max : value);
    }
    
    public static long clampLong(long value, long min, long max)
    {
        return value < min ? min : (value > max ? max : value);
    }

    public static float clampFloat(float value, float min, float max)
    {
        return value < min ? min : (value > max ? max : value);
    }
    
    public static double clampDouble(double value, double min, double max)
    {
        return value < min ? min : (value > max ? max : value);
    }  
}
