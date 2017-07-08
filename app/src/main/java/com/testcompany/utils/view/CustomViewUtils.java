package com.testcompany.utils.view;

public class CustomViewUtils{

    public static String setFont(int textFont,boolean isInEditMode) {
        String textFontType = null;
        if (!isInEditMode) {
            switch (textFont) {
                case 1:
                    textFontType = "OpenSans_Bold.ttf";
                    break;
                case 2:
                    textFontType = "OpenSans_BoldItalic.ttf";
                    break;
                case 3:
                    textFontType = "OpenSans_ExtraBold.ttf";
                    break;
                case 4:
                    textFontType = "OpenSans_ExtraBoldItalic.ttf";
                    break;
                case 5:
                    textFontType = "OpenSans_Italic.ttf";
                    break;
                case 6:
                    textFontType = "OpenSans_Light.ttf";
                    break;
                case 7:
                    textFontType = "OpenSans_LightItalic.ttf";
                    break;
                case 8:
                    textFontType = "OpenSans_Regular.ttf";
                    break;
                case 9:
                    textFontType = "OpenSans_Semibold.ttf";
                    break;
                case 10:
                    textFontType = "OpenSans_SemiboldItalic.ttf";
                    break;
                default:
                    textFontType = "OpenSans_Regular.ttf";
                    break;
            }

        }
        return textFontType;

    }
}