package com.king.app.roles.conf;

/**
 * Desc:
 *
 * @author：Jing Yang
 * @date: 2019/8/5 14:39
 */
public class StoryType {
    public static final int C_C = 0; // chapter, character
    public static final int R_K_C_C = 1; // race, kingdom, chapter, character

    public static final String[] typeTexts = new String[]{
            "Chapter + Character",
            "Chapter + Character + Race + Kingdom"
    };

    public static String getStoryType(int type) {
        return typeTexts[type];
    }

}
