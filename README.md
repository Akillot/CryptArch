# www.cryptarch.org

## API Endpoints

### Crypto
- **Get cryptocurrency overview**  
  `GET /api/crypto/bitcoin/overview?fiat=usd&days=7`

- **Get price of a specific cryptocurrency**  
  `GET /api/crypto/price/bitcoin/usd`

- **Get prices of multiple cryptocurrencies**  
  `GET /api/crypto/prices?symbols=bitcoin,ethereum,solana&fiat=usd`

- **Get list of all cryptocurrencies**  
  `GET /api/crypto/list`

### News
- **Get news by keyword**  
  `GET /api/news?query=bitcoin&from=2025-08-01T00:00:00Z&to=2025-08-12T00:00:00Z`
