import android.content.Context
import com.shaan.androiduicomponents.models.University
import com.shaan.androiduicomponents.UniversityListActivity

object ShortlistManager {
    private const val PREF_NAME = "shortlist_preferences"
    private const val KEY_SHORTLISTED = "shortlisted_universities"

    fun getShortlistedUniversities(context: Context): Set<String> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_SHORTLISTED, emptySet()) ?: emptySet()
    }

    fun addToShortlist(context: Context, universityName: String) {
        val currentList = getShortlistedUniversities(context).toMutableSet()
        currentList.add(universityName)
        saveShortlist(context, currentList)
    }

    fun removeFromShortlist(context: Context, universityName: String) {
        val currentList = getShortlistedUniversities(context).toMutableSet()
        currentList.remove(universityName)
        saveShortlist(context, currentList)
    }

    private fun saveShortlist(context: Context, universities: Set<String>) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putStringSet(KEY_SHORTLISTED, universities).apply()
    }

    fun getShortlistedUniversityObjects(context: Context): List<University> {
        val shortlistedNames = getShortlistedUniversities(context)
        return UniversityListActivity.universities.filter {
            shortlistedNames.contains(it.name) 
        }
    }
}