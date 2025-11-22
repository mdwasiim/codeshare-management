export interface MenuItem {
  id: number;
  name: string;
  icon?: string;
  route: string;
  children?: MenuItem[];
}
