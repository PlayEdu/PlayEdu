function depsAreSame(oldDeps, deps) {
    if (oldDeps === deps) {
        return true;
    }
    for (var i = 0; i < oldDeps.length; i++) {
        if (!Object.is(oldDeps[i], deps[i])) {
            return false;
        }
    }
    return true;
}
export default depsAreSame;
