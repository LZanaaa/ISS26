async function loadRemoteCode(url, targetId) {
    try {
        const response = await fetch(url);
        if (!response.ok) throw new Error("File non trovato");
        const text = await response.text();
        const element = document.getElementById(targetId);

        // Inseriamo il testo puro (textContent gestisce i caratteri < > automaticamente)
        element.textContent = text;

        // Applichiamo la colorazione Java di Prism
        if (typeof Prism !== 'undefined') {
            Prism.highlightElement(element);
        }
    } catch (error) {
        document.getElementById(targetId).textContent = "Errore nel caricamento: " + error.message;
    }
}

// Eseguiamo il caricamento per ogni file
const urlICell = "https://raw.githubusercontent.com/LZanaaa/ISS26/refs/heads/main/ConwayLife/Sprint1/conway26Java/src/main/java/conway/domain/ICell.java";
const urlIGrid = "https://raw.githubusercontent.com/LZanaaa/ISS26/refs/heads/main/ConwayLife/Sprint1/conway26Java/src/main/java/conway/domain/IGrid.java";

loadRemoteCode(urlICell, "code-icell");
loadRemoteCode(urlIGrid, "code-igrid");
loadRemoteCode(urlILife, "code-lifeinterface");
loadRemoteCode(urlOutDev, "code-outdev");
loadRemoteCode(urlGame, "code-game");
