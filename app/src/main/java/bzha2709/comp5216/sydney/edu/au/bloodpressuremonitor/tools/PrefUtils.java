package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.tools;

import android.content.Context;
import android.content.SharedPreferences;

import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.R;

public class PrefUtils
{
    public static String preference_file_key;
    public static void ini(Context con)
    {
        preference_file_key= con.getString(R.string.preference_file_key);
    }

    public static void putBoolean(Context context,String key,Boolean values){
        SharedPreferences Pref= context.getSharedPreferences(preference_file_key,Context.MODE_PRIVATE);
        Pref.edit().putBoolean(key,values).apply();
    }
    public static Boolean getBoolean(Context context,String key,Boolean defvalues){
        SharedPreferences Pref= context.getSharedPreferences(preference_file_key,Context.MODE_PRIVATE);
        return  Pref.getBoolean(key,defvalues);
    }

    public static void putlong(Context context,String key,long values){
        SharedPreferences Pref= context.getSharedPreferences(preference_file_key,Context.MODE_PRIVATE);
        Pref.edit().putLong(key,values).apply();
    }

    public static long getlong(Context context,String key,long defvalues){
        SharedPreferences Pref= context.getSharedPreferences(preference_file_key,Context.MODE_PRIVATE);
        return Pref.getLong(key,defvalues);
    }
    public static void remove(Context context,String key){
        SharedPreferences Pref= context.getSharedPreferences(preference_file_key,Context.MODE_PRIVATE);
        Pref.edit().remove(key);
    }
    public static void putString(Context context,String key,String values){
        SharedPreferences Pref= context.getSharedPreferences(preference_file_key,Context.MODE_PRIVATE);
        Pref.edit().putString(key, values).apply();
    }
    public static String getString(Context context,String key,String defvalues){
        SharedPreferences Pref= context.getSharedPreferences(preference_file_key,Context.MODE_PRIVATE);
        return Pref.getString(key,defvalues);
    }

    public static void putInt(Context context,String key,int values){
        SharedPreferences Pref= context.getSharedPreferences(preference_file_key,Context.MODE_PRIVATE);
        Pref.edit().putInt(key, values).apply();
    }
    public static int getInt(Context context,String key,int defvalues){
        SharedPreferences Pref= context.getSharedPreferences(preference_file_key, Context.MODE_PRIVATE);
        return Pref.getInt(key, defvalues);
    }
}
