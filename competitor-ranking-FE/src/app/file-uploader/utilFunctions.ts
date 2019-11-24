export function nullAndSizeCheck(name: any, size: number) {
    if ((name && name.length > size)) {
        return true;
    }
    return false;
}