export function getNameFromCookie() {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${"name"}=`);
  if (parts.length === 2) return parts.pop()!.split(";").shift();
}

export function checkIfNameCookieExists(): boolean {
  return getCookie("name");
}

/**
 * Function to delete a cookie by settings its expiry date to 1970
 *
 * @param path Path of cookie - Not required
 * @param domain Domain of cookie - Not required
 */
export function deleteName(path?: string, domain?: string) {
  if (getCookie("name")) {
    document.cookie =
      "name" +
      "=" +
      (path ? ";path=" + path : "") +
      (domain ? ";domain=" + domain : "") +
      ";expires=Thu, 01 Jan 1970 00:00:01 GMT";
  }
}

function getCookie(name: string) {
  return document.cookie.split(";").some((c) => {
    return c.trim().startsWith(name + "=");
  });
}
