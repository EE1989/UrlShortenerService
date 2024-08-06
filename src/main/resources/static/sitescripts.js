
    function copyToClipboard() {
        var copyText = document.getElementById("shortenedUrl");
        var textArea = document.createElement("textarea");
        textArea.value = copyText.textContent;
        document.body.appendChild(textArea);
        textArea.select();
        document.execCommand('copy');
        document.body.removeChild(textArea);
        alert("URL copied to clipboard!");
    }
