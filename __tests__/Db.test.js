import dbconnection from '../library/db.js';

database = null;

test('Create database', () => {
	database = new dbconnection();
	result = database.checkStatus("open");
	//console.log(database.checkStatus("all"));
	expect(result).toBe(true);
});