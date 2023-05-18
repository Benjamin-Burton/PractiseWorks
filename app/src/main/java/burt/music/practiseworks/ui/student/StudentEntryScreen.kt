/*
 * Copyright (C) 2022 The Android Open Source Project
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

package burt.music.practiseworks.ui.student

// Date entry box imports for material 3
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import burt.music.practiseworks.ui.AppViewModelProvider
import burt.music.practiseworks.ui.PractiseWorksTopAppBar
import burt.music.practiseworks.ui.item.ItemEntryBody
import burt.music.practiseworks.ui.item.ItemEntryDestination
import burt.music.practiseworks.ui.item.ItemUiState
import burt.music.practiseworks.ui.navigation.NavigationDestination
import burt.music.practiseworks.ui.theme.PractiseWorksTheme
import com.example.practiseworks.R
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

object StudentEntryDestination : NavigationDestination {
    override val route = "student_entry"
    override val titleRes = R.string.student_entry_title
}

@Composable
fun StudentEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true,
    viewModel: StudentEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            PractiseWorksTopAppBar(
                title = stringResource(ItemEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        StudentEntryBody(
            studentUiState = viewModel.studentUiState,
            onStudentValueChange = viewModel::updateUiState,
            onSaveClick = { coroutineScope.launch {
                viewModel.saveStudent()
                navigateBack()
            }},
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun StudentEntryBody(
    studentUiState: StudentUiState,
    onStudentValueChange: (StudentUiState) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier

) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        StudentInputForm(studentUiState = studentUiState, onValueChange = onStudentValueChange)
        Button(
            onClick = onSaveClick,
            enabled = studentUiState.actionEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action))
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StudentInputForm(
    studentUiState: StudentUiState,
    modifier: Modifier = Modifier,
    onValueChange: (StudentUiState) -> Unit = {},
    enabled: Boolean = true
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {

        OutlinedTextField(
            value = studentUiState.f_name,
            onValueChange = { onValueChange(studentUiState.copy(f_name = it)) },
            label = { Text(stringResource(R.string.student_f_name_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = studentUiState.l_name,
            onValueChange = { onValueChange(studentUiState.copy(l_name = it)) },
            label = { Text(stringResource(R.string.student_l_name_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = studentUiState.username,
            onValueChange = { onValueChange(studentUiState.copy(username = it))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(R.string.student_username_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = studentUiState.email,
            onValueChange = { onValueChange(studentUiState.copy(email = it)) },
            label = { Text(stringResource(R.string.student_email_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = studentUiState.password,
            onValueChange = { onValueChange(studentUiState.copy(password = it)) },
            label = { Text(stringResource(R.string.student_password_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = studentUiState.teacher_id.toString(),
            onValueChange = { onValueChange(studentUiState.copy(teacher_id = it.toInt())) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(R.string.student_teacher_id_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = studentUiState.practiseGoal.toString(),
            onValueChange = { onValueChange(studentUiState.copy(practiseGoal = it.toInt())) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("Practise Amount Weekly Goal") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
//        DatePickerField(
//            date = studentUiState.join_date,
//            onValueChange = { onValueChange(studentUiState.copy(join_date = it)) }
//        )
        val listItems = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            modifier = Modifier.fillMaxWidth(),
            onExpandedChange = { isExpanded = it },

        ) {
            TextField(
                value = studentUiState.lesson_day,
                onValueChange = { },
                readOnly = true,
                label = { Text(text = "Lesson Day") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false })
            {
                listItems.forEach { selectedOption ->
                    DropdownMenuItem(onClick = {
                        onValueChange(studentUiState.copy(lesson_day = selectedOption))
                        isExpanded = false
                    }) {
                        Text(text = selectedOption)
                    }
                }
            }
        }
        OutlinedTextField(
            value = studentUiState.lesson_time,
            onValueChange = { onValueChange(studentUiState.copy(lesson_time = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = { Text(stringResource(R.string.student_lesson_time_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
    }
}

//private fun showDatePicker(
//    activity : ComponentActivity,
//    updatedDate: (Date) -> Unit)
//{
//    val picker = MaterialDatePicker.Builder.datePicker().build()
//    picker.show()
//    picker.addOnPositiveButtonClickListener {
//        updatedDate(dateFormater(it))
//    }
//}
//
//fun dateFormater(milliseconds : Long) : Date{
//    milliseconds.let {
//        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.UK)
//        val calendar: Calendar = Calendar.getInstance()
//        calendar.setTimeInMillis(it)
//        return Date(milliseconds)
//    }
//}

@Preview(showBackground = true)
@Composable
private fun StudentEntryScreenPreview() {
    PractiseWorksTheme {
        ItemEntryBody(
            itemUiState = ItemUiState(
                name = "Item name",
                price = "10.00",
                quantity = "5"
            ),
            onItemValueChange = {},
            onSaveClick = {}
        )
    }
}
