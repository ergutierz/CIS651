package com.example.cis651syracuse.core

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.cis651syracuse.project1.model.GameScore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val Context.dataStore by preferencesDataStore(name = "game_prefs")

    private val gson = Gson()

    suspend fun saveGameScores(scores: List<GameScore>) {
        val scoresJson = gson.toJson(scores)
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey("game_scores")] = scoresJson
        }
    }

    val gameScores: Flow<List<GameScore>> = context.dataStore.data.map { preferences ->
        val scoresJson = preferences[stringPreferencesKey("game_scores")].orEmpty()
        if (scoresJson.isNotEmpty()) {
            val type: Type = object : TypeToken<List<GameScore>>() {}.type
            gson.fromJson(scoresJson, type)
        } else {
            emptyList()
        }
    }
}