package com.example.cis651syracuse.project3

sealed class Screen(val route: String) {
    object SpeechTranslationScreen : Screen("speech_translation_screen")
    object ConversationScreen : Screen("conversation_screen")
    object LearningPathScreen : Screen("learning_path_screen")
    object VocabularyExercisesScreen : Screen("vocabulary_exercises_screen")
    object GrammarTipsScreen : Screen("grammar_tips_screen")
    object ImmersionFeaturesScreen : Screen("immersion_features_screen")
    object ProgressTrackingScreen : Screen("progress_tracking_screen")
    object CommunityScreen : Screen("community_screen")
    object AccessibilitySettingsScreen : Screen("accessibility_settings_screen")
    object PrivacySettingsScreen : Screen("privacy_settings_screen")
    object DashboardScreen : Screen("dashboard_screen")
    object LoginScreen : Screen("login_screen")
    object RegisterScreen : Screen("register_screen")

    companion object {
        fun getScreenTitle(route: String?): String {
            return when (route) {
                SpeechTranslationScreen.route -> "Speech Translation"
                ConversationScreen.route -> "Interactive Conversations"
                LearningPathScreen.route -> "Learning Path"
                VocabularyExercisesScreen.route -> "Vocabulary Exercises"
                GrammarTipsScreen.route -> "Grammar Tips"
                ImmersionFeaturesScreen.route -> "Language Immersion"
                ProgressTrackingScreen.route -> "Progress Tracking"
                CommunityScreen.route -> "Community"
                AccessibilitySettingsScreen.route -> "Accessibility Settings"
                PrivacySettingsScreen.route -> "Privacy and Security"
                RegisterScreen.route -> "Register"
                LoginScreen.route -> "Login"
                else -> "Speech Buddy"
            }
        }
    }
}
