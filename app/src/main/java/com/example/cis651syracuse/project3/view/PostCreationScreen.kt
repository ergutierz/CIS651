package com.example.cis651syracuse.project3.view

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.cis651syracuse.core.ErrorDialog
import com.example.cis651syracuse.core.InputField
import com.example.cis651syracuse.core.LoadingBar
import com.example.cis651syracuse.core.SuccessDialog
import com.example.cis651syracuse.core.handleEvent
import com.example.cis651syracuse.project3.model.Post
import com.example.cis651syracuse.project3.viewmodel.PostCreationViewModel
import java.io.File
import java.io.IOException

@Composable
fun PostCreationScreen() {
    val viewModel: PostCreationViewModel = hiltViewModel()
    val viewState: PostCreationViewModel.ViewState by viewModel.viewState.collectAsState()
    val context = LocalContext.current
    var description by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            val tempUri = saveImageToTempFile(context, it)
            imageUri = tempUri
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Create a New Post", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        InputField(
            onValueChange = { description = it },
            label = "Description",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        imageUri?.let {
            Image(painter = rememberAsyncImagePainter(it), contentDescription = "Selected Image", modifier = Modifier.size(128.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { pickImageLauncher.launch("image/*") }) {
            Text("Pick Image from Gallery")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { takePictureLauncher.launch(null) }) {
            Text("Capture Image from Camera")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.onAction(
                    PostCreationViewModel.Action.CreatePost(
                        description = description,
                        imageUri = imageUri.toString()
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Post")
        }
    }

    ConsumeEvent(viewState)
    if (viewState.isLoading) LoadingBar()
}

private fun saveImageToTempFile(context: Context, bitmap: Bitmap): Uri {
    // Create a temporary file
    val tempFile = File(context.cacheDir, "tempImage_${System.currentTimeMillis()}.jpg")
    try {
        tempFile.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", tempFile)
}


@Composable
private fun ConsumeEvent(viewState: PostCreationViewModel.ViewState) = with(viewState) {
    viewState.consumableEvent.handleEvent { event ->
        when (event) {
            is PostCreationViewModel.Event.PostCreationSuccess -> SuccessDialog()
            is PostCreationViewModel.Event.Error -> ErrorDialog()
        }
    }
}
