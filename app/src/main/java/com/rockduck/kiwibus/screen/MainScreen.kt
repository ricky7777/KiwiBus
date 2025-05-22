package com.rockduck.kiwibus.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rockduck.kiwibus.data.RouteData
import com.rockduck.kiwibus.model.BusModel
import kotlin.math.roundToInt

/**
 * Main Screen
 */
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var searchQuery by remember { mutableStateOf("") }
    val busesFilterResult = remember(searchQuery) {
        RouteData.busModels.filter { it.routeShortName.contains(searchQuery) }
    }
    val selectedBus = remember { mutableStateOf<BusModel?>(null) }
    SearchScreen(
        modifier = modifier.fillMaxSize(),
        searchQuery = searchQuery,
        onSearchQueryChange = { searchQuery = it },
        busesFilterResult = busesFilterResult,
        selectedBus = selectedBus
    )

    selectedBus.value?.let { bus ->
        ShowBusInfoDialog(
            bus = bus,
            onDismiss = { selectedBus.value = null }
        )
    }
}

@Composable
private fun SearchScreen(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    busesFilterResult: List<BusModel>,
    selectedBus: MutableState<BusModel?>
) {
    val configuration = LocalConfiguration.current
    val screenHeightPx = configuration.screenHeightDp
    val topMargin: Dp = ((screenHeightPx * 0.15f).roundToInt()).dp
    Column(modifier = modifier) {
        SearchInput(topMargin = topMargin, searchQuery, onSearchQueryChange)
        Spacer(modifier = Modifier.height(16.dp))
        BusList(busesFilterResult, selectedBus, searchQuery)
    }
}

@Composable
private fun SearchInput(
    topMargin: Dp,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = topMargin, start = 8.dp, end = 8.dp)
    ) {
        SearchBar(
            searchQuery = searchQuery,
            onQueryChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun BusList(
    buses: List<BusModel>,
    selectedBus: MutableState<BusModel?>,
    searchQuery: String
) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        if (searchQuery.isNotEmpty()) {
            items(buses) { bus ->
                ShowBuses(
                    bus = bus,
                    isSelected = (bus == selectedBus.value),
                    onClick = { selectedBus.value = bus }
                )
            }
        }
    }
}

@Composable
private fun ShowBusInfoDialog(
    bus: BusModel,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        },
        title = {
            Text("Bus Information")
        },
        text = {
            Column {
                Text("Route: ${bus.routeShortName}")
                Text("Description: ${bus.routeShortName}")
                Text("Agency: ${bus.agencyId}")
            }
        }
    )
}

@Composable
private fun ShowBuses(
    bus: BusModel,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFAED581) else Color(0xFFDCEDC8)
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(
            text = "Bus: ${bus.routeShortName}",
            modifier = Modifier.padding(16.dp),
            color = if (isSelected) Color(0xFF1B5E20) else Color(0xFF33691E),
            style = MaterialTheme.typography.bodyLarge
        )
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