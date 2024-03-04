package com.example.cis651syracuse.project3.view

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.cis651syracuse.core.*
import com.example.cis651syracuse.project3.viewmodel.PostEditViewModel
import java.io.File
import java.io.IOException

@Composable
fun PostEditScreen() {
    val viewModel: PostEditViewModel = hiltViewModel()
    val viewState by viewModel.viewState.collectAsState()
    val context = LocalContext.current

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.onAction(PostEditViewModel.Action.UpdateImageUri(it))
        }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            val tempUri = saveImageToTempFile(context, it)
            viewModel.onAction(PostEditViewModel.Action.UpdateImageUri(tempUri))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Edit Post", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        InputFieldPrePopulated(
            onValueChange = {
                viewModel.onAction(PostEditViewModel.Action.UpdateDescription(it))
            },
            label = "Description",
            modifier = Modifier.fillMaxWidth(),
            initialValue = viewState.description.orEmpty()
        )

        Spacer(modifier = Modifier.height(8.dp))

        viewState.imageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Selected Image",
                modifier = Modifier.size(128.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { pickImageLauncher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pick Image from Gallery")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { takePictureLauncher.launch(null) }, modifier = Modifier.fillMaxWidth()) {
            Text("Capture Image from Camera")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.onAction(PostEditViewModel.Action.UpdatePost)
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Update Post")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.onAction(PostEditViewModel.Action.DeletePost) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Delete Post")
        }
    }

    ConsumeEvent(viewState)
    if (viewState.isLoading) LoadingBar()

    LaunchedEffect("loadPosts") {
        viewModel.onAction(PostEditViewModel.Action.LoadPost)
    }
}

private fun saveImageToTempFile(context: Context, bitmap: Bitmap): Uri {
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
private fun ConsumeEvent(viewState: PostEditViewModel.ViewState) = with(viewState) {
    consumableEvent.handleEvent { event ->
        when (event) {
            is PostEditViewModel.Event.PostEditSuccess -> SuccessDialog()
            is PostEditViewModel.Event.PostEditFailure -> ErrorDialog()
        }
    }
}