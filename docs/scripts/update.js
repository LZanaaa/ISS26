async function loadRemoteCode(url, targetId) {
    try {
        const response = await fetch(url);
        if (!response.ok) throw new Error("File non trovato");
        const text = await response.text();
        const element = document.getElementById(targetId);

        element.textContent = text;

        if (typeof Prism !== 'undefined') {
            Prism.highlightElement(element);
        }
    } catch (error) {
        document.getElementById(targetId).textContent = "Errore nel caricamento: " + error.message;
    }
}

// Funzione per mostrare/nascondere il codice
function toggleCode(codeId) {
    const preElement = document.getElementById("pre-" + codeId);
    if (preElement.style.display === "none") {
        preElement.style.display = "block";
    } else {
        preElement.style.display = "none";
    }
}

// Configurazione URL base (Raw) e caricamento dei file
const baseUrl = "https://raw.githubusercontent.com/LZanaaa/ISS26/refs/heads/main/ConwayLife/Sprint1/conway26Java/src/main/java/conway/domain/";

// Lista di tutti i file da caricare
loadRemoteCode(baseUrl + "ICell.java", "code-icell");
loadRemoteCode(baseUrl + "Cell.java", "code-cell");
loadRemoteCode(baseUrl + "IGrid.java", "code-igrid");
loadRemoteCode(baseUrl + "Grid.java", "code-grid");
loadRemoteCode(baseUrl + "LifeInterface.java", "code-lifeinterface");
loadRemoteCode(baseUrl + "Life.java", "code-life");
loadRemoteCode(baseUrl + "GameController.java", "code-gamecontroller");
loadRemoteCode(baseUrl + "LifeController.java", "code-lifecontroller");
loadRemoteCode(baseUrl + "IOutDev.java", "code-outdev");