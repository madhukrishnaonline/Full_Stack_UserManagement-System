export interface TodoModelContract {
    id?: number,
    name: string,
    description: string,
    startTime: string | null,
    endTime: string | null,
    completed: boolean | null,
    userId?: string
}