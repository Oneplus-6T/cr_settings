/*
 * Copyright (C) 2023-2024 The risingOS Android Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.crdroid.settings.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.provider.Settings;
import android.util.AttributeSet;
import com.android.settings.R;
import android.widget.Toast;

import com.android.internal.util.crdroid.ThemeUtils;

public class AdaptivePreferenceUtils {

    private static final String overlayThemeTarget  = "com.android.systemui";
    private static final String SETTINGS_THEME_PROP  = "settings_theme_style";

    public static void refreshTheme(Context context) {
        final ThemeUtils themeUtils = new ThemeUtils(context);
        Toast.makeText(context, context.getString(R.string.reevaluating_theme), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                themeUtils.setOverlayEnabled("android.theme.customization.sysui_reevaluate", overlayThemeTarget, overlayThemeTarget);
                themeUtils.setOverlayEnabled("android.theme.customization.sysui_reevaluate", "com.android.system.qs.sysui_reevaluate", overlayThemeTarget);
            }
        }, Toast.LENGTH_SHORT + 500L);
    }

    public static String getPosition(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AdaptivePreference);
        String positionAttribute = typedArray.getString(R.styleable.AdaptivePreference_position);
        typedArray.recycle();

        return positionAttribute;
    }
    
    public static boolean isLineageSettings(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AdaptivePreference);
        boolean isLineage = typedArray.getBoolean(R.styleable.AdaptivePreference_isLineageSettings, false);
        typedArray.recycle();

        return isLineage;
    }

    public static int getLayoutResourceId(Context context, AttributeSet attrs) {
        final String positionString = getPosition(context, attrs);
        return getLayoutResourceId(context, positionString, false);
    }
    
    public static int getLayoutResourceId(Context context, String positionString, boolean isHomePage) {
        int defaultSettingsTheme = isHomePage ? Settings.System.getInt(context.getContentResolver(), SETTINGS_THEME_PROP, 0) : 0;
        final Position position = Position.fromAttribute(positionString);

        if (positionString != null && positionString.equals("wellbeing")) {
            switch (defaultSettingsTheme) {
                case 0:
                default:
                    return R.layout.top_level_preference_wellbeing_card;
                case 1:
                    return R.layout.top_level_preference_wellbeing_ayan;
                case 2:
                    return R.layout.top_level_preference_wellbeing_card_material;
            }
        } else if (positionString != null && positionString.equals("google")) {
            switch (defaultSettingsTheme) {
                case 0:
                default:
                    return R.layout.top_level_preference_google_card;
                case 1:
                    return R.layout.top_level_preference_google_ayan;
                case 2:
                    return R.layout.top_level_preference_google_card_material;
            }
        }

        if (position == null) {
            switch (defaultSettingsTheme) {
                case 0:
                default:
                    return R.layout.top_level_preference_middle_card;
                case 1:
                    return R.layout.top_level_preference_middle_ayan;
                case 2:
                    return R.layout.top_level_preference_middle_card_material;
            }
        }

        switch (position) {
            case TOP:
                switch (defaultSettingsTheme) {
                    case 0:
                    default:
                        return R.layout.top_level_preference_top_card;
                    case 1:
                        return R.layout.top_level_preference_top_ayan;
                    case 2:
                        return R.layout.top_level_preference_top_card_material;
                }
            case BOTTOM:
                switch (defaultSettingsTheme) {
                    case 0:
                    default:
                        return R.layout.top_level_preference_bottom_card;
                    case 1:
                        return R.layout.top_level_preference_bottom_ayan;
                    case 2:
                        return R.layout.top_level_preference_bottom_card_material;
                }
            case MIDDLE:
                switch (defaultSettingsTheme) {
                    case 0:
                    default:
                        return R.layout.top_level_preference_middle_card;
                    case 1:
                        return R.layout.top_level_preference_middle_ayan;
                    case 2:
                        return R.layout.top_level_preference_middle_card_material;
                }
            default:
            case SOLO:
                return R.layout.top_level_preference_solo_card;
            case NONE:
                return -1;
        }
    }

    public static int getSeekBarLayoutResourceId(Context context, AttributeSet attrs) {
        final String positionString = getPosition(context, attrs);
        final Position position = Position.fromAttribute(positionString);
        
        if (position == null) {
            return R.layout.preference_custom_seekbar_middle;
        }

        switch (position) {
            case TOP:
                return R.layout.preference_custom_seekbar_top;
            case BOTTOM:
                return R.layout.preference_custom_seekbar_bottom;
            case MIDDLE:
                return R.layout.preference_custom_seekbar_middle;
            case SOLO:
                return R.layout.preference_custom_seekbar_solo;
            case NONE:
                return -1;
            default:
                return R.layout.preference_custom_seekbar_solo;
        }
    }

    public enum Position {
        TOP,
        MIDDLE,
        BOTTOM,
        SOLO,
        NONE;

        public static Position fromAttribute(String attribute) {
            if (attribute != null) {
                switch (attribute.toLowerCase()) {
                    case "top":
                        return TOP;
                    case "bottom":
                        return BOTTOM;
                    case "middle":
                        return MIDDLE;
                    case "solo":
                        return SOLO;
                    case "none":
                        return NONE;
                    default:
                        return null;
                }
            }
            return null;
        }
    }
}
