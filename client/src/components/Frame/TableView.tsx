import { ReactNode } from 'react';

export type TableColumn = {
  key: string;
  label: string | ReactNode;
};

export type TableRow = {
  [key: string]: React.ReactNode;
};

interface TableViewProps {
  columns: TableColumn[];
  rows: TableRow[];
  onRowClick?: (row: TableRow) => void;
}

const TableView = ({ columns, rows, onRowClick }: TableViewProps) => {
  return (
    <div className='w-full h-full'>
      <table className='w-full h-full border-separate border-spacing-0'>
        <thead className='bg-light-cyan text-black'>
          <tr>
            {columns.map((column, idx) => (
              <th
                key={column.key}
                className={`
              px-4 py-3 text-p1 font-semibold border-b border-new-grey text-center
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
            <tr
              key={index}
              className={`hover:bg-light-cyan-30 ${onRowClick ? 'cursor-pointer' : ''}`}
              onClick={() => onRowClick?.(row)}
            >
              {columns.map((column) => (
                <td
                  key={column.key}
                  className='px-4 py-2 text-p1 font-regular border-b border-new-grey'
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
