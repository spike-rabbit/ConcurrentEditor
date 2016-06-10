String mergeText(String current, Change change) {
	String result;
	switch(change.type) {
		case Type.INSERT:
			result = current.substring(0, change.startIndex) + change.text + current.substring(startIndex, current.length-1);
			break;
		case Type.DELETE:
			//Text steht an der Stelle wo er sollte
			if(change.text == current.substring(change.startIndex, change.length-1)) {
				result = current.substring(0, change.startIndex) + current.substring(startIndex, current.length-1);
			}
			//Text hat sich wegen unbekannter Änderungen verschoben. Gefahr, dass Zeichen gelöscht werden, die gar nicht gelöscht werden sollten.
			else {
				int leftMatch = 0;
				int rightMatch = 0;
				for(int i = 0; change.startIndex - i >= 0 && i + startIndex < current.length; i++) {
					if(change.startIndex - i >= 0 && text.charAt(leftMatch) == current.charAt(startIndex - i)) {
						leftMatch++;
					}
					if(i + startIndex < current.length && text.charAt(rightMatch) == current.charAt(startIndex + 1)) {
						rightMatch++;
					}
					
					if(leftMatch == text.length) {
						result = current.substring(0,change.startIndex - leftMatch) + current.substring(change.startIndex - leftMatch + change.text.length, current.length);
					} else if(rightMatch == text.length) {
						result = current.substring(0, change.startIndex + rightMatch) + current.substring(change.startIndex + rightMatch + text.length);
					}
				} 
			}
			break;
	}
}