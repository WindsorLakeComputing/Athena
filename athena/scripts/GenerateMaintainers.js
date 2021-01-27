var faker = require("../frontend/node_modules/faker/index.js")

const numOfMaintainers = parseInt(process.argv[2])
if(Number.isNaN(numOfMaintainers)) {
  throw Error("Arg must be a number!")
}

const fakeSection = () => {
  const index = faker.random.number(5)
  switch (index) {
    case 0: return "Weapons";
    case 1: return "APG";
    case 2: return "Production";
    case 3: return "Specialists";
    default: return "Support";
  }
}

const fakePref = () => {
  const index = faker.random.number(3)
  switch (index) {
    case 0: return "D,S,M";
    case 1: return "M,D,S";
    default: return "S,M,D";
  }
}

const fakeShift = () => {
  const index = faker.random.number(3)
  switch (index) {
    case 0: return "D";
    case 1: return "M";
    default: return "S";
  }
}

let output = "lastName,firstName,employeeNumber,section,courseCode,assignedShift,firstPreference,secondPreference,thirdPreference,level,rank"

for(var i = 0; i < numOfMaintainers; i++){
  output = output.concat(`\n${faker.name.lastName()},${faker.name.firstName()},${faker.random.number()},${fakeSection()},002052,${fakeShift()},${fakePref()},${2*(faker.random.number(4)+1)-1},${faker.random.alphaNumeric(5).toUpperCase()}`)
}

console.log(output);