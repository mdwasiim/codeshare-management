export interface CrudToolbarAction {
    key: string;
    label: string;
    icon: string;
    severity?: 'primary' | 'secondary' | 'success' | 'info' | 'warn' | 'danger';
    outlined?: boolean;
    disabled?: boolean;
    visible?: boolean;
}
