function agregarManual() {
    const codigo = document.getElementById('codigoManual').value.trim();
    if (!codigo) {
        Swal.fire('Código vacío', 'Por favor ingresa un código válido.', 'warning');
        return;
    }

    fetch('/Marvela/scan', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: 'code=' + encodeURIComponent(codigo)
    })
    .then(response => response.text())
    .then(resultado => {
    if (resultado.includes('Código no encontrado')) {
        Swal.fire('No encontrado', 'El código ingresado no existe en la base de datos.', 'info');
    } else if (resultado.includes('todas las unidades disponibles')) {
        Swal.fire('Stock máximo alcanzado', 'Ya escaneaste todas las unidades disponibles de este producto.', 'warning');
    }  else if (resultado.includes('no tiene unidades disponibles')) {
    Swal.fire({
        toast: true,
        position: 'bottom-end',
        icon: 'error',
        title: 'Sin producto en el stock',
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true
    });
}else {
        actualizarTabla();
        document.getElementById('codigoManual').value = '';
    }
})
    .catch(error => {
        console.error('Error al consultar código:', error);
        Swal.fire('Error', 'No se pudo consultar el producto.', 'error');
    });
}


function actualizarCantidad(codigo, nuevaCantidad) {
    fetch('/Marvela/actualizarCantidad', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ codigo, cantidad: nuevaCantidad })
    })
    .then(res => res.json())
    .then(data => {
        if (data.exito) {
            calcularTotal(); // ✅ recalcula el total en pantalla
        }
    });
}

function quitarProducto(codigo) {
    fetch('/Marvela/quitarProducto', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ codigo })
    })
    .then(res => res.json())
    .then(data => {
        if (data.exito) {
            actualizarTabla();
        }
    });
}

function calcularTotal() {
    let total = 0;
    document.querySelectorAll('#tabla-container tbody tr').forEach(row => {
        const cantidadInput = row.querySelector('.cantidad-input');
        const precioTexto = row.cells[4]?.textContent?.replace('$', '').trim();

        if (cantidadInput && precioTexto) {
            const cantidad = parseInt(cantidadInput.value);
            const precio = parseFloat(precioTexto);
            if (!isNaN(cantidad) && !isNaN(precio)) {
                total += cantidad * precio;
            }
        }
    });

    const totalInput = document.getElementById('totalVenta');
    if (totalInput) {
        totalInput.value = `$${total.toFixed(2)}`;
    }
}

function vaciarProductos() {
    fetch('/Marvela/vaciarEscaneos', {
        method: 'POST'
    })
    .then(res => res.json())
    .then(data => {
        if (data.exito) {
            actualizarTabla();
        }
    });
}



function finalizarVenta() {
  // 🔄 Primero sincroniza la lista estática con la sesión
  fetch("/Marvela/sincronizar", { method: "POST" })
    .then(() => {
      // ✅ Luego registra la venta
      fetch("/Marvela/RegistrarVentaServlet", { method: "POST" })
        .then(res => res.json())
        .then(data => {
          if (data.exito) {
            const fecha = new Date().toLocaleDateString('es-ES', {
              day: '2-digit',
              month: 'long',
              year: 'numeric'
            });

            document.getElementById('venta-info').innerHTML =
              `<p class="text-muted mb-4">Venta #${data.numeroVenta} | ${fecha} | ${data.cantidadTotal} productos</p>`;

            Swal.fire('¡Venta registrada!', `Venta #${data.numeroVenta}`, 'success');

            // ✅ Abrir el ticket PDF usando el número de venta
            const url = `/Marvela/finalizarVenta?numeroVenta=${data.numeroVenta}`;
            window.open(url, "_blank");

            vaciarProductos();
          } else if (data.mensaje === "No se puede registrar una venta sin productos.") {
            Swal.fire({
              icon: 'warning',
              title: 'Venta vacía',
              text: 'No puedes registrar una venta sin productos escaneados.',
              toast: true,
              position: 'bottom-end',
              timer: 3000,
              showConfirmButton: false
            });
          } else {
            Swal.fire('Error', 'No se pudo registrar la venta.', 'error');
          }
        })
        .catch(err => {
          console.error(err);
          Swal.fire('Error', 'Ocurrió un problema al registrar la venta.', 'error');
        });
    })
    .catch(err => {
      console.error("❌ Error al sincronizar productos:", err);
      Swal.fire('Error', 'No se pudo preparar la venta.', 'error');
    });
}
function actualizarNumeroVenta() {
    fetch('/Marvela/obtenerNumeroVenta')
        .then(res => res.json())
        .then(data => {
            if (data.numeroVenta) {
                const fecha = new Date().toLocaleDateString('es-ES', {
                    day: '2-digit',
                    month: 'long',
                    year: 'numeric'
                });

                document.getElementById('venta-info').innerHTML =
                    `<p class="text-muted mb-4">Venta #${data.numeroVenta} | ${fecha}</p>`;
            } else {
                document.getElementById('venta-info').innerHTML =
                    `<p class="text-danger">No se pudo obtener el número de venta.</p>`;
            }
        })
        .catch(err => {
            console.error("❌ Error al obtener número de venta:", err);
            document.getElementById('venta-info').innerHTML =
                `<p class="text-danger">Error al consultar número de venta.</p>`;
        });
}

function actualizarTabla() {
    fetch('/Marvela/scan')
        .then(response => response.text())
        .then(html => {
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');

            // ✅ Actualiza tabla
            const nuevaTabla = doc.getElementById('tabla-container').innerHTML;
            document.getElementById('tabla-container').innerHTML = nuevaTabla;

            // ✅ Recalcula total
            setTimeout(() => calcularTotal(), 50);

            // ✅ Vuelve a solicitar el número de venta
            actualizarNumeroVenta(); // ← esta es la clave
        })
        .catch(error => console.error('Error al actualizar tabla:', error));
}

function actualizarNumeroVenta() {
    fetch('/Marvela/obtenerNumeroVenta')
        .then(res => res.json())
        .then(data => {
            if (data.numeroVenta) {
                const fecha = new Date().toLocaleDateString('es-ES', {
                    day: '2-digit',
                    month: 'long',
                    year: 'numeric'
                });

                document.getElementById('venta-info').innerHTML =
                    `<p class="text-muted mb-4">Venta #${data.numeroVenta} | ${fecha}</p>`;
            } else {
                document.getElementById('venta-info').innerHTML =
                    `<p class="text-danger">No se pudo obtener el número de venta.</p>`;
            }
        })
        .catch(err => {
            console.error("❌ Error al obtener número de venta:", err);
            document.getElementById('venta-info').innerHTML =
                `<p class="text-danger">Error al consultar número de venta.</p>`;
        });
}
document.querySelectorAll('.cantidad-input').forEach(input => {
  input.addEventListener('change', () => {
    const nuevaCantidad = parseInt(input.value);
    const codigo = input.dataset.codigo;

    if (!isNaN(nuevaCantidad) && nuevaCantidad > 0) {
      actualizarCantidad(codigo, nuevaCantidad);
    }
  });
});

window.addEventListener('DOMContentLoaded', () => {
    actualizarNumeroVenta();
});
// Recalcular total cada 3 segundos por si hay cambios manuales
setInterval(calcularTotal, 3000);