package com.example.ofn.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NavigationPanel(text:String, clickAction: ()->Unit){
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(10.dp, CircleShape)
            .background(MaterialTheme.colors.background, CircleShape)
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clickable { clickAction.invoke() },
        horizontalArrangement = Arrangement.SpaceBetween,
    ){
        Text(
            text = text,
            textAlign = TextAlign.Start
        )
        Icon(Icons.Outlined.ArrowForward, contentDescription = "Next")
    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NavigationPanel(text = "Hil") {
        
    }
}