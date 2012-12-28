package com.some.locallife.providers;

import android.content.SearchRecentSuggestionsProvider;

public class LocalQuerySuggestionsProvider extends
		SearchRecentSuggestionsProvider {

	public static final String AUTHORITY = "com.some.locallife.providers.LocalQuerySuggestionsProvider";
	public static final int MODE = DATABASE_MODE_QUERIES;

	public LocalQuerySuggestionsProvider() {
		super();
		setupSuggestions(this.AUTHORITY, this.MODE);
	}
}
