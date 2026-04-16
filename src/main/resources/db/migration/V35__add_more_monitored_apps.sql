ALTER TABLE record_sessions
    DROP CONSTRAINT IF EXISTS ck_record_sessions_app_id;

ALTER TABLE record_sessions
    ADD CONSTRAINT ck_record_sessions_app_id
        CHECK (
            app_id IS NULL
                OR app_id IN (
                    'VSCODE',
                    'WEBSTORM',
                    'INTELLIJ_IDEA',
                    'PYCHARM',
                    'GOLAND',
                    'PHPSTORM',
                    'RUBYMINE',
                    'CLION',
                    'RIDER',
                    'ANDROID_STUDIO',
                    'XCODE',
                    'UNITY',
                    'BLENDER',
                    'DATAGRIP',
                    'NOTION',
                    'FIGMA',
                    'ROBLOX_STUDIO'
                )
        );

ALTER TABLE record_session_segments
    DROP CONSTRAINT IF EXISTS ck_record_session_segments_app_id;

ALTER TABLE record_session_segments
    ADD CONSTRAINT ck_record_session_segments_app_id
        CHECK (
            app_id IN (
                'VSCODE',
                'WEBSTORM',
                'INTELLIJ_IDEA',
                'PYCHARM',
                'GOLAND',
                'PHPSTORM',
                'RUBYMINE',
                'CLION',
                'RIDER',
                'ANDROID_STUDIO',
                'XCODE',
                'UNITY',
                'BLENDER',
                'DATAGRIP',
                'NOTION',
                'FIGMA',
                'ROBLOX_STUDIO'
            )
        );

ALTER TABLE record_develop_sessions_v2
    DROP CONSTRAINT IF EXISTS ck_record_develop_sessions_v2_app_id;

ALTER TABLE record_develop_sessions_v2
    ADD CONSTRAINT ck_record_develop_sessions_v2_app_id
        CHECK (
            app_id IN (
                'VSCODE',
                'WEBSTORM',
                'INTELLIJ_IDEA',
                'PYCHARM',
                'GOLAND',
                'PHPSTORM',
                'RUBYMINE',
                'CLION',
                'RIDER',
                'ANDROID_STUDIO',
                'XCODE',
                'UNITY',
                'BLENDER',
                'DATAGRIP',
                'NOTION',
                'FIGMA',
                'ROBLOX_STUDIO'
            )
        );

ALTER TABLE record_develop_session_segments_v2
    DROP CONSTRAINT IF EXISTS ck_record_develop_session_segments_v2_app_id;

ALTER TABLE record_develop_session_segments_v2
    ADD CONSTRAINT ck_record_develop_session_segments_v2_app_id
        CHECK (
            app_id IN (
                'VSCODE',
                'WEBSTORM',
                'INTELLIJ_IDEA',
                'PYCHARM',
                'GOLAND',
                'PHPSTORM',
                'RUBYMINE',
                'CLION',
                'RIDER',
                'ANDROID_STUDIO',
                'XCODE',
                'UNITY',
                'BLENDER',
                'DATAGRIP',
                'NOTION',
                'FIGMA',
                'ROBLOX_STUDIO'
            )
        );
