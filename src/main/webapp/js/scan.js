function enviarCodigo() {
    const input = document.getElementById("codigoInput");
    const codigo = input.value.trim();

    if (!codigo) {
        alert("Por favor ingresa un código válido.");
        return;
    }

    fetch("http://localhost:8080/Marvela/scan", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: "code=" + encodeURIComponent(codigo)
    })
    .then(response => {
        if (!response.ok) throw new Error("Error en la respuesta del servidor");
        return response.text();
    })
    .then(html => {
        document.getElementById("contenedorEscaneos").innerHTML = html;
        input.value = "";
        input.focus();
    })
    .catch(error => {
        console.error("❌ Error al enviar código:", error);
        alert("Error al conectar con el servidor.");
    });
}