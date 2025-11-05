$(document).ready(function () {
  $("#ingresar").on("submit", function (e) {
    e.preventDefault();
    var correo = $("#correo").val();
    var clave = $("#clave").val();

    if (!correo || !clave) {
      Swal.fire('Alerta', "Por favor ingresa correo y clave", "warning");
      return;
    }

    var datos = {
      consultar_datos: "ingresar",
      correo: correo,
      clave: clave
    };

    $.ajax({
      dataType: "json",
      method: "POST",
      url: "UsuarioController",
      data: datos
    }).done(function (json) {
      if (json[0].resultado === "exito") {
        mensaje("Ingresado con éxito", "BIENVENIDO A FERRETERÍA MARVELA", "success");
        setTimeout(() => {
          window.location.href = "index.jsp";
        }, 800);
      } else {
        Swal.fire('Alerta', json[0].mensaje, "error");
      }
    }).fail(function () {
      Swal.fire('Error', "No se pudo conectar con el servidor", "error");
    });
  });
});

// ✅ Función separada fuera del ready
function mensaje(titulo, mensaje, icono) {
  Swal.fire({
    title: titulo,
    text: mensaje,
    icon: icono,
    timer: 800
  });
}