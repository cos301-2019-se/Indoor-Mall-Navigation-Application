import { SQLite } from 'expo';

export default class dbconnection{
	database: null;
	created: false;
	filled: false;
	open: false;
	constructor(props)
	{
		this.database = SQLite.openDatabase("indoors");
		this.open = true;
		//console.log("opened");
		this.dropTable();
		this.buildTable();
		this.fillTable();
		//console.log(this.created);
	}

	checkStatus(property = "all"){
		if(property == "all")
		{
			return [this.open, this.created, this.filled];
		}else if(property == "open")
		{
			return this.open;
		}else if(property == "created")
		{
			return this.created;
		}else if(property == "filled")
		{
			return this.filled;
		}else
		{
			return false;
		}

	}
	dropTable (){
		this.database.transaction(tx => {
			tx.executeSql(
				"DROP TABLE IF EXISTS tbl_items",
				[],
				(tx, success)=>{console.log("DB created");}, 
				(tx, error)=>{console.log("DB create error " + error);}
			);
		});
	}

	buildTable (){
		this.database.transaction(tx => {
			tx.executeSql(
				"CREATE TABLE IF NOT EXISTS tbl_items('barcode_id' INTEGER PRIMARY KEY,'name' TEXT(50) )",
				[],
				(tx, success)=>{console.log("DB created");}, 
				(tx, error)=>{console.log("DB create error " + error);}
			);
		});
	}
	fillTable (){
		this.database.transaction(tx => {
			tx.executeSql(
				"INSERT INTO tbl_items(barcode_id, name)VALUES(1, 'Shoes'), (2, 'Sweater'), (3, 'Hoody'), ('60097736', 'Zam-Buk')",
				[],
				(tx, success)=>{console.log("DB Filled");}, 
				(tx, error)=>{console.log("DB fill error " + error);}
			);
		});
	}

	checkBarcode(barcode) {
		alert(barcode)
		this.database.transaction(tx => {
			tx.executeSql(
				"SELECT * FROM tbl_items WHERE barcode_id = ?",
				[barcode],
				(tx, success)=>{alert(success.rows.item(0).name)}, 
				(tx, error)=>{alert("failed to query " + error)}
			);
		});
	}
}