package com.connor.tasks.ui.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.connor.tasks.R

@Composable
fun AddTask(onDismiss: () -> Unit, cancelIconClick: () -> Unit, saveClick: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                DialogHead(cancelIconClick = cancelIconClick, saveClick = saveClick)
                Spacer(modifier = Modifier.height(24.dp))
                DialogBody()
            }
        },
        text = { },
        confirmButton = {

        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogBody() {
    var text by remember { mutableStateOf("") }
    Column {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text(text = "New task") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(18.dp))
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            Image(
                painterResource(id = R.drawable.schedule),
                contentDescription = "schedule",
                modifier = Modifier.size(32.dp)
            )
            Image(
                painterResource(id = R.drawable.favorite),
                contentDescription = "schedule",
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(32.dp)
            )
        }
    }
}

@Composable
fun DialogHead(cancelIconClick: () -> Unit, saveClick: () -> Unit) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (icon, tvTitle, tvSave) = createRefs()
        Icon(
            modifier = Modifier
                .constrainAs(icon) {
                    start.linkTo(parent.start)
                }
                .size(36.dp)
                .clickable(onClick = cancelIconClick),
            imageVector = Icons.Filled.Close,
            contentDescription = "close",
            tint = Gray
        )
        Text(
            text = "Add Task",
            fontSize = 22.sp,
            modifier = Modifier.constrainAs(tvTitle) {
                start.linkTo(icon.end, margin = 16.dp)
                top.linkTo(icon.top)
                bottom.linkTo(icon.bottom)
            }
        )
        Text(
            text = "Save",
            color = colorResource(id = R.color.google_blue),
            fontSize = 18.sp,
            modifier = Modifier
                .constrainAs(tvSave) {
                    end.linkTo(parent.end, margin = 8.dp)
                    top.linkTo(tvTitle.top)
                    bottom.linkTo(tvTitle.bottom)
                }
                .clickable(onClick = saveClick)
        )
    }
}