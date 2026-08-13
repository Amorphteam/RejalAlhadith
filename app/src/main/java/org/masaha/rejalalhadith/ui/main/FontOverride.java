package org.masaha.rejalalhadith.ui.main;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import java.lang.reflect.Field;

/**
 * Created by vamsi on 06-05-2017 for Android custom font article
 */
public class FontOverride {
    public static void setDefaultFont(Context context,
                                      String staticTypefaceFieldName, String fontAssetName) {
        setDefaultFont(context, staticTypefaceFieldName, fontAssetName, 400);
    }

    public static void setDefaultFont(Context context,
                                      String staticTypefaceFieldName,
                                      String fontAssetName,
                                      int weight) {
        replaceFont(staticTypefaceFieldName, createTypeface(context, fontAssetName, weight));
    }

    private static Typeface createTypeface(Context context, String fontAssetName, int weight) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Typeface.Builder(context.getAssets(), fontAssetName)
                    .setFontVariationSettings("'wght' " + weight)
                    .setWeight(weight)
                    .build();
        }

        Typeface regular = Typeface.createFromAsset(context.getAssets(), fontAssetName);
        if (weight >= 600) {
            return Typeface.create(regular, Typeface.BOLD);
        }
        return regular;
    }

    protected static void replaceFont(String staticTypefaceFieldName,
                                      final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
