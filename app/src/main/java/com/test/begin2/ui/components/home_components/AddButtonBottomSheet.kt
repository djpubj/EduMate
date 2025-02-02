package com.test.begin2.ui.components.home_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.test.begin2.ui.presentation.home.BottomSheetItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddButtonBottomSheet(
    BottomSheetItemList: List<BottomSheetItem>,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(BottomSheetItemList) { item ->
                BottomSheetRepresentaion(title = item.title, onClick = item.onclick)
            }
        }
    }
}