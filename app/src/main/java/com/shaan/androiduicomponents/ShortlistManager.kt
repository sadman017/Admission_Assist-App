import android.content.Context
import com.shaan.androiduicomponents.models.University
import com.shaan.androiduicomponents.UniversityListActivity

object ShortlistManager {
    private const val PREF_NAME = "shortlist_preferences"
    private const val KEY_SHORTLISTED = "shortlisted_universities"

    fun addToShortlist(context: Context, universityName: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val currentShortlist = getShortlistedUniversities(context).toMutableSet()
        currentShortlist.add(universityName)
        
        sharedPreferences.edit().putStringSet(KEY_SHORTLISTED, currentShortlist).apply()
    }

    fun removeFromShortlist(context: Context, universityName: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val currentShortlist = getShortlistedUniversities(context).toMutableSet()
        currentShortlist.remove(universityName)
        
        sharedPreferences.edit().putStringSet(KEY_SHORTLISTED, currentShortlist).apply()
    }

    fun getShortlistedUniversities(context: Context): Set<String> {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getStringSet(KEY_SHORTLISTED, emptySet()) ?: emptySet()
    }

    fun isUniversityShortlisted(context: Context, universityName: String): Boolean {
        return getShortlistedUniversities(context).contains(universityName)
    }

    fun getShortlistedUniversityList(context: Context, allUniversities: List<University>): List<University> {
        val shortlistedNames = getShortlistedUniversities(context)
        return allUniversities.filter { university -> 
            shortlistedNames.contains(university.generalInfo.name) 
        }
    }
}