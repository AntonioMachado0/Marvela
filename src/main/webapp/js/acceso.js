$("#ingresar").on("submit", function (e) {
    e.preventDefault();
    var usuario = $("#usuario").val();
    var clave = $("#clave").val();

    var datos = {
        consultar_datos: "ingresar",
        usuario: usuario,
        clave: clave
    };

    $.ajax({
        dataType: "json",
        method: "POST",
        url: "UsuarioController",
        data: datos
    }).done(function (json) {
        if (json[0].resultado === "exito") {
            mensaje("Ingresado con exito", "BIENVENIDO A GESTION DE TAREAS", "success");
            window.location.href = "index.jsp";
        } else {
            Swal.fire('Alerta', "Error en las credenciales...", "error");
        }
    }).fail(function () {
    }).always(function () {
    });
});

function mensaje(titulo, mensaje, icono) {
    Swal.fire({
        title: titulo,
        text: mensaje,
        icon: icono,
        timer: 1000000
    });
}