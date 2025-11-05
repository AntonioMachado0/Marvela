//function salir() {
//    // Invalida la sesión en el lado del cliente si es posible
//    fetch('UsuarioController?consultar_datos=salir', {method: 'POST'})
//            .then(response => {
//                if (response.ok) {
//                    return response.json();
//                } else {
//                    throw new Error('Error al cerrar sesión');
//                }
//            })
//            .then(data => {
//                if (data.resultado === "exito") {
//                    // Redirige a la página de inicio de sesión
//                    window.location.href = 'acceso.jsp';
//                } else {
//                    throw new Error('Error al cerrar sesión: ' + data.mensaje);
//                }
//            })
//            .catch(error => {
//                console.error(error);
//                // Redirige a la página de inicio de sesión en caso de error
//                window.location.href = 'acceso.jsp';
//            });
//}

/**
 * Cierra la sesión del usuario y redirige al login,
 * evitando que pueda regresar con la flecha del navegador.
 * Limpia almacenamiento local y reemplaza historial.
 */
function salir() {
    // Limpia almacenamiento local si se usa
    localStorage.clear();
    sessionStorage.clear();

    // Llama al backend para cerrar sesión
    fetch('UsuarioController?consultar_datos=salir', { method: 'POST' })
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al cerrar sesión');
            }
            return response.json();
        })
        .then(data => {
            if (data.resultado === "exito") {
                // Redirige reemplazando historial para evitar flecha atrás
                window.location.replace('acceso.jsp');
            } else {
                throw new Error('Error al cerrar sesión: ' + data.mensaje);
            }
        })
        .catch(error => {
            console.error('Fallo al cerrar sesión:', error);
            // Redirige igual en caso de error
            window.location.replace('acceso.jsp');
        });
}