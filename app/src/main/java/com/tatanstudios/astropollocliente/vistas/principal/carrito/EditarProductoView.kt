package com.tatanstudios.astropollocliente.vistas.principal.carrito

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tatanstudios.astropollocliente.extras.TokenManager
import kotlinx.coroutines.flow.first
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tatanstudios.astropollocliente.R
import com.tatanstudios.astropollocliente.componentes.BarraToolbarColor
import com.tatanstudios.astropollocliente.componentes.CustomToasty
import com.tatanstudios.astropollocliente.componentes.LoadingModal
import com.tatanstudios.astropollocliente.componentes.ToastType
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tatanstudios.astropollocliente.componentes.BarraToolbarColorMenuPrincipal
import com.tatanstudios.astropollocliente.componentes.CustomModal1Boton
import com.tatanstudios.astropollocliente.componentes.CustomModal1BotonTitulo
import com.tatanstudios.astropollocliente.model.modelos.ModeloInformacionProductoArray
import com.tatanstudios.astropollocliente.model.modelos.ModeloProductosTerceraArray
import com.tatanstudios.astropollocliente.network.RetrofitBuilder
import com.tatanstudios.astropollocliente.viewmodel.EnviarProductoAlCarritoViewModel
import com.tatanstudios.astropollocliente.viewmodel.InformacionProductoEditadoViewModel
import com.tatanstudios.astropollocliente.viewmodel.InformacionProductoViewModel
import java.util.Locale

@Composable
fun EditarProductoScreen(
    navController: NavHostController,
    idFilaProducto: Int,
    viewModel: InformacionProductoEditadoViewModel = viewModel(),
) {
    val ctx = LocalContext.current
    val tokenManager = remember { TokenManager(ctx) }
    val isLoading by viewModel.isLoading.observeAsState(true)
    val resultado by viewModel.resultado.observeAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    // MODAL 1 BOTON
    var showModal1Boton by remember { mutableStateOf(false) }
    var modalMensajeString by remember { mutableStateOf("") }
    var modalTituloString by remember { mutableStateOf("") }


    // estado UI
    var producto by remember { mutableStateOf<ModeloInformacionProductoArray?>(null) }
    var cantidad by remember { mutableStateOf(1) }
    var nota by remember { mutableStateOf("") }
    var errorNotaObligatoria by remember { mutableStateOf(false) }
    var idusuario by remember { mutableStateOf("") }

    // cargar datos
    LaunchedEffect(idFilaProducto) {
        //viewModel.informacionProductoRetrofit(idProducto)
    }

    // ocultar teclado al entrar
    LaunchedEffect(Unit) {
        idusuario = tokenManager.idUsuario.first()
        keyboardController?.hide()
    }


    Scaffold(
        topBar = {
            BarraToolbarColor(
                navController,
                stringResource(R.string.editar_cantidad),
                colorResource(R.color.colorRojo)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {


            if (isLoading) LoadingModal(isLoading = true)


            if(showModal1Boton){
                CustomModal1BotonTitulo(showModal1Boton, modalTituloString, modalMensajeString, onDismiss = {showModal1Boton = false})
            }

        }
    }



}
