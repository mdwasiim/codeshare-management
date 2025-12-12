export interface CSMMenuItemModel {
  label: string;
  icon?: string;
  routerLink?: string | string[];
  url?: string;                     // <-- ADD THIS
  target?: string;                  // <-- ADD THIS
  class?: string;                   // <-- used for rotated-icon
  items?: CSMMenuItemModel[];
  visible?: boolean;
  separator?: boolean;
  styleClass?: string;              // <-- existing style class support
  badgeClass?: string;              // <-- used in some menu items
}
