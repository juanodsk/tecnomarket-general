export function hasPermission(permissions: string[], permission: string) {
  return permissions.includes(permission);
}

export function hasRole(roles: string[], role: string) {
  return roles.includes(role);
}
