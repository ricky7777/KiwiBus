package com.rockduck.kiwibus.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rockduck.kiwibus.data.RouteData
import kotlin.math.roundToInt

/**
 * Main Screen
 */
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredList = remember(searchQuery) {
        RouteData.busModels.filter { it.routeShortName.contains(searchQuery) }
    }

    val configuration = LocalConfiguration.current
    val screenHeightPx = configuration.screenHeightDp
    val topMargin: Dp = ((screenHeightPx * 0.15f).roundToInt()).dp

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = topMargin, start = 8.dp, end = 8.dp)
        ) {
            SearchBar(
                searchQuery = searchQuery,
                onQueryChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            if (!searchQuery.isEmpty()) {
                items(filteredList) { bus ->
                    Text("Bus: ${bus.routeShortName}")
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Type Bus Number"
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = { query ->
            val filtered = query.filter { it.isDigit() }
            if (filtered.length <= 5) {
                onQueryChange(filtered)
            }
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        placeholder = { Text(placeholder) },
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )
}