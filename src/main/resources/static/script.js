const apiBaseUrl = "http://localhost:8081";

async function fetchCryptoPrice(symbol) {
    try {
        const response = await fetch(`${apiBaseUrl}/api/external/crypto/price/${symbol}`);
        if (!response.ok) throw new Error(`Error: ${response.status}`);
        const data = await response.json();

        document.getElementById("price").textContent = `Price of ${symbol}: ${data.price || "no data"}`;
    } catch (error) {
        console.error("Error fetching price:", error);
    }
}

async function fetchCryptoNews(cryptoId, from, to) {
    try {
        const url = `${apiBaseUrl}/api/news/${cryptoId}?from=${from}&to=${to}`;
        const response = await fetch(url);
        if (!response.ok) throw new Error(`Error: ${response.status}`);
        const news = await response.json();

        const newsList = document.getElementById("newsList");
        newsList.innerHTML = "";

        if (news.length === 0) {
            newsList.innerHTML = "<li>No news for the selected period.</li>";
            return;
        }

        news.forEach(item => {
            const li = document.createElement("li");
            li.textContent = `${item.title} (${item.publishedAt})`;
            newsList.appendChild(li);
        });
    } catch (error) {
        console.error("Error fetching news:", error);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("loadPrice").addEventListener("click", () => {
        const symbol = document.getElementById("symbolInput").value.trim();
        if (symbol) fetchCryptoPrice(symbol);
    });

    document.getElementById("loadNews").addEventListener("click", () => {
        const cryptoId = document.getElementById("cryptoIdInput").value.trim();
        const from = document.getElementById("fromDate").value;
        const to = document.getElementById("toDate").value;

        if (cryptoId && from && to) {
            fetchCryptoNews(cryptoId, from, to);
        }
    });
});
