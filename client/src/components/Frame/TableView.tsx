export type TableColumn = {
  key: string;
  label: string;
};

export type TableRow = {
  [key: string]: React.ReactNode;
};

interface TableViewProps {
  columns: TableColumn[];
  rows: TableRow[];
}

const TableView = ({ columns, rows }: TableViewProps) => {
  return (
    <div className='w-full h-full'>
      <table className='w-full h-full border-separate border-spacing-0'>
        <thead className='bg-light-cyan text-black'>
          <tr>
            {columns.map((column, idx) => (
              <th
                key={column.key}
                className={`
              px-4 py-3 text-p2 font-semibold border-b border-new-grey text-center
              ${idx === 0 ? 'rounded-tl-xl' : ''}
              ${idx === columns.length - 1 ? 'rounded-tr-xl' : ''}
            `}
              >
                {column.label}
              </th>
            ))}
          </tr>
        </thead>
        <tbody className='bg-white'>
          {rows.map((row, index) => (
            <tr key={index}>
              {columns.map((column) => (
                <td
                  key={column.key}
                  className='px-4 py-2 text-p2 font-regular border-b border-new-grey'
                >
                  <div className='flex justify-center items-center'>{row[column.key]}</div>
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default TableView;
