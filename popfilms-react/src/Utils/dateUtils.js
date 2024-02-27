function convertEpochToDate(epoch) {
    const date = new Date(epoch);
    const month = date.getMonth() + 1;
    const day = date.getDate();
    const year = date.getFullYear();

    const formattedMonth = month < 10 ? '0' + month : month;
    const formattedDay = day < 10 ? '0' + day : day;

    return formattedMonth + '/' + formattedDay + '/' + year;
}
export default convertEpochToDate;