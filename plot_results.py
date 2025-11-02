import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

sns.set_style("whitegrid")
plt.rcParams['figure.figsize'] = (14, 10)
plt.rcParams['font.size'] = 11

df = pd.read_csv('benchmark_results.csv')

fig, (ax1, ax2) = plt.subplots(2, 1, figsize=(14, 10))

ax1.plot(df['Database_Size'], df['HashMap_Operations'], 
         marker='o', linewidth=2, markersize=8, label='HashMap')
ax1.plot(df['Database_Size'], df['TreeMap_Operations'], 
         marker='s', linewidth=2, markersize=8, label='TreeMap')
ax1.plot(df['Database_Size'], df['Heap_Operations'], 
         marker='^', linewidth=2, markersize=8, label='Heap')

ax1.set_xlabel('Розмір бази даних (кількість студентів)', fontsize=12, fontweight='bold')
ax1.set_ylabel('Кількість операцій за 10 секунд', fontsize=12, fontweight='bold')
ax1.set_title('Продуктивність контейнерів (A:B:C = 2:10:30)', 
              fontsize=14, fontweight='bold', pad=20)
ax1.legend(fontsize=11, loc='best')
ax1.grid(True, alpha=0.3)
ax1.set_xscale('log')
ax1.set_yscale('log')

for container in ['HashMap', 'TreeMap', 'Heap']:
    for i, row in df.iterrows():
        ops = row[f'{container}_Operations']
        ax1.annotate(f'{ops:,}', 
                    xy=(row['Database_Size'], ops),
                    xytext=(5, 5), textcoords='offset points',
                    fontsize=8, alpha=0.7)

x = range(len(df))
width = 0.25

ax2.bar([i - width for i in x], df['HashMap_Memory_MB'], 
        width, label='HashMap', alpha=0.8)
ax2.bar([i for i in x], df['TreeMap_Memory_MB'], 
        width, label='TreeMap', alpha=0.8)
ax2.bar([i + width for i in x], df['Heap_Memory_MB'], 
        width, label='Heap', alpha=0.8)

ax2.set_xlabel('Розмір бази даних (кількість студентів)', fontsize=12, fontweight='bold')
ax2.set_ylabel('Використана пам\'ять (MB)', fontsize=12, fontweight='bold')
ax2.set_title('Використання пам\'яті', fontsize=14, fontweight='bold', pad=20)
ax2.set_xticks(x)
ax2.set_xticklabels(df['Database_Size'])
ax2.legend(fontsize=11, loc='best')
ax2.grid(True, alpha=0.3, axis='y')

for i, row in df.iterrows():
    for j, container in enumerate(['HashMap', 'TreeMap', 'Heap']):
        mem = row[f'{container}_Memory_MB']
        ax2.text(i + (j-1)*width, mem + 2, f'{mem}MB',
                ha='center', va='bottom', fontsize=9)

plt.tight_layout()
plt.savefig('benchmark_results.png', dpi=300, bbox_inches='tight')
print("Графіки збережено у benchmark_results.png")

print("\n" + "="*80)
print("ПОРІВНЯЛЬНА ТАБЛИЦЯ")
print("="*80)
print(df.to_string(index=False))
print("="*80)

print("\nВИСНОВКИ:")

for _, row in df.iterrows():
    size = row['Database_Size']
    ops = {
        'HashMap': row['HashMap_Operations'],
        'TreeMap': row['TreeMap_Operations'],
        'Heap': row['Heap_Operations']
    }
    best = max(ops, key=ops.get)
    print(f"  База {size:>6} студентів: {best} показав найкращий результат "
          f"({ops[best]:,} операцій)")