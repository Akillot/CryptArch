const apiBaseUrl = "http://localhost:8081";
let priceChart;
let priceUpdateInterval;

function createPriceChart() {
    const ctx = document.getElementById("priceChart").getContext("2d");
    priceChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: [],
            datasets: [{
                label: 'Price (USD)',
                data: [],
                borderColor: '#1a73e8',
                backgroundColor: 'rgba(26, 115, 232, 0.1)',
                tension: 0.3
            }]
        },
        options: {
            responsive: true,
            plugins: { legend: { labels: { color: '#222' } } },
            scales: {
                x: { ticks: { color: '#222' } },
                y: { ticks: { color: '#222' } }
            }
        }
    });
}

async function fetchCryptoPrice(symbol) {
    try {
        const response = await fetch(`${apiBaseUrl}/api/external/crypto/price/${symbol}`);
        if (!response.ok) throw new Error(`Error: ${response.status}`);
        const data = await response.json();
        document.getElementById("price").textContent = `Price of ${symbol}: ${data.price || "No data"}`;
        return data.price || 0;
    } catch (error) {
        console.error("Error fetching price:", error);
        return 0;
    }
}

async function updatePriceChart(symbol) {
    const price = await fetchCryptoPrice(symbol);
    const time = new Date().toLocaleTimeString();

    priceChart.data.labels.push(time);
    priceChart.data.datasets[0].data.push(price);

    if (priceChart.data.labels.length > 20) {
        priceChart.data.labels.shift();
        priceChart.data.datasets[0].data.shift();
    }

    priceChart.update();
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
    createPriceChart();

    document.getElementById("loadPrice").addEventListener("click", () => {
        const symbol = document.getElementById("symbolInput").value.trim();
        if (symbol) {
            clearInterval(priceUpdateInterval);
            updatePriceChart(symbol);
            priceUpdateInterval = setInterval(() => updatePriceChart(symbol), 300000);
        }
    });

    document.getElementById("loadNews").addEventListener("click", () => {
        const cryptoId = document.getElementById("cryptoIdInput").value.trim();
        const from = document.getElementById("fromDate").value;
        const to = document.getElementById("toDate").value;

        if (cryptoId && from && to) fetchCryptoNews(cryptoId, from, to);
    });
});
