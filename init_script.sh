# Create player Bogdan
curl -vX POST http://localhost:8080/api/player -d @setup-data/bogdan.json \
--header "Content-Type: application/json"

# Create player Gilberto
curl -vX POST http://localhost:8080/api/player -d @setup-data/gilberto.json \
--header "Content-Type: application/json"

# Create a game between Bogdan and Gilberto
curl -vX POST http://localhost:8080/api/game -d @setup-data/game.json \
--header "Content-Type: application/json"