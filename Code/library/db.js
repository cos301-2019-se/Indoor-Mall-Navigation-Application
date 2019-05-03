import { SQLite } from 'expo';

export default class dbconnection{
	database: null;
	constructor(props)
	{
		database = SQLite.openDatabase("indoors");
		this.buildTable();
		this.fillTable();
		
	}

	buildTable (){
		database.transaction(tx => {
			tx.executeSql(
				"CREATE TABLE IF NOT EXISTS tbl_items('barcode_id' INTEGER PRIMARY KEY AUTOINCREMENT,'name' TEXT(50) )",
				[],
				(tx, success)=>{alert("created");}, 
				(tx, error)=>{alert("failed to create " + error)}
			);
		});
	}
	fillTable (){
		database.transaction(tx => {
			tx.executeSql(
				"INSERT INTO tbl_items(name)VALUES('Shoes'), ('Hamburger'), ('Cellphone')",
				[],
				(tx, success)=>{alert("filled")}, 
				(tx, error)=>{alert("failed to fill " + error)}
			);
		});
	}

	checkBarcode(barcode) {
		alert(barcode)
		database.transaction(tx => {
			tx.executeSql(
				"SELECT * FROM tbl_items WHERE barcode_id = ?",
				[barcode],
				(tx, success)=>{alert(success.rows.item(0).name)}, 
				(tx, error)=>{alert("failed to query " + error)}
			);
		});
	}
}