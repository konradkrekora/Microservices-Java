const ItemType = {
	PLAYER: "player",
	CASTLE: "castle",
	SOLDIER: "soldier",
	EMPTY: "empty",
};

const rowItem = {
	type: ItemType.PLAYER,
	playerId: 2,
	content: "P",
};

const rowItemPlayer1 = {
	type: ItemType.PLAYER,
	playerId: 1,
	content: "P1",
};

const rowItemPlayer2 = {
	type: ItemType.PLAYER,
	playerId: 2,
	content: "P2",
};

const rowItemCastle = {
	type: ItemType.CASTLE,
	playerId: 1,
	content: "C1",
};

const rowItemSoldier1 = {
	type: ItemType.SOLDIER,
	playerId: 1,
	content: "S1",
};

const rowItemSoldier2 = {
	type: ItemType.SOLDIER,
	playerId: 2,
	content: "S2",
};

const battleArea = document.getElementById("battle-area");

const renderBattleRow = (response) => {
	response.map((battleRowArray) => {
		const row = document.createElement("div");
		row.className = "row";
		battleRowArray.map((rowElement) => {
			const square = document.createElement("div");
			square.innerText = rowElement.content;
			square.className = `square ${rowElement.type + rowElement.playerId}`;
			row.appendChild(square);
		});
		battleArea.appendChild(row);
	});
};

//renderBattleRow();

const fetchData = () => {
	console.log("start");
	fetch("http://localhost:8081/getUnits")
		.then((response) => response.json())
		.then((response) => {
			console.log("response", response);
			// tutaj zawołasz renderowanie jak podmienisz adres
			renderBattleRow(response);
		})
		.catch(function (err) {
			// There was an error
			console.warn("Something went wrong.", err);
		});
};
fetchData();
