# Campus Connect 🎓

A modern campus social platform built with React, TypeScript, and Convex that connects students and teachers in an interactive learning environment.

## ✨ Features

### 🏠 Social Feed
- **Post Creation**: Share text posts and images with the campus community
- **Image Upload**: Upload and share photos with automatic storage handling
- **Real-time Updates**: Live feed updates using Convex's reactive database
- **Role-based Display**: Teachers get special badges (⭐) on their posts

### 📅 Events & Hackathons
- **Event Management**: Teachers can create and manage campus events
- **Event Discovery**: Students can browse upcoming events and hackathons
- **Detailed Information**: Rich event descriptions with dates and organizer info
- **Real-time Notifications**: Instant updates when new events are posted

### 💬 Q&A System
- **Student Queries**: Students can ask questions to teachers
- **Teacher Responses**: Teachers can answer student questions in real-time
- **Threaded Conversations**: Organized question-answer format
- **Status Tracking**: Clear indication of answered vs pending questions

### 👤 Profile Management
- **Role-based Profiles**: Separate profiles for students and teachers
- **Student Information**: Class, section, and bio management
- **Teacher Profiles**: Specialized teacher profile with bio
- **Profile Editing**: Easy-to-use profile update interface

### 🔐 Authentication
- **Secure Login**: Username/password authentication via Convex Auth
- **Role Setup**: First-time user role selection and profile creation
- **Session Management**: Persistent login sessions

## 🛠️ Tech Stack

### Frontend
- **React 18** - Modern React with hooks
- **TypeScript** - Type-safe development
- **Tailwind CSS** - Utility-first CSS framework
- **Vite** - Fast build tool and dev server

### Backend
- **Convex** - Reactive database with real-time updates
- **Convex Auth** - Authentication system
- **File Storage** - Built-in file storage for images

### Key Libraries
- `convex` - Database and real-time functionality
- `@convex-dev/auth` - Authentication
- `clsx` & `tailwind-merge` - CSS utility management

## 🚀 Getting Started

### Prerequisites
- Node.js 18+ 
- npm or yarn

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd campus-connect
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Set up Convex**
   ```bash
   npx convex dev
   ```
   This will:
   - Create a new Convex project
   - Set up the database schema
   - Configure authentication

4. **Start the development server**
   ```bash
   npm run dev
   ```

5. **Open your browser**
   Navigate to `http://localhost:5173`

## 📁 Project Structure

```
campus-connect/
├── src/
│   ├── components/           # React components
│   │   ├── Dashboard.tsx     # Main dashboard layout
│   │   ├── FeedPage.tsx      # Social feed component
│   │   ├── EventsPage.tsx    # Events management
│   │   ├── QueryPage.tsx     # Q&A system
│   │   ├── ProfilePage.tsx   # User profiles
│   │   └── RoleSetup.tsx     # Initial role selection
│   ├── lib/
│   │   └── utils.ts          # Utility functions
│   ├── App.tsx               # Main app component
│   ├── main.tsx              # App entry point
│   └── index.css             # Global styles
├── convex/
│   ├── schema.ts             # Database schema
│   ├── auth.ts               # Authentication config
│   ├── posts.ts              # Post-related functions
│   ├── events.ts             # Event-related functions
│   ├── queries.ts            # Q&A functions
│   ├── profiles.ts           # Profile functions
│   └── http.ts               # HTTP handlers
└── public/                   # Static assets
```

## 🗄️ Database Schema

### Users Table (Auth)
- `name` - User's full name
- `email` - Email address
- `image` - Profile image (optional)

### Profiles Table
- `userId` - Reference to user
- `role` - "student" or "teacher"
- `class` - Student's class (students only)
- `section` - Student's section (students only)
- `bio` - User biography

### Posts Table
- `userId` - Post author
- `content` - Post text content
- `imageId` - Reference to stored image (optional)
- `userName` - Cached user name
- `userRole` - Cached user role

### Events Table
- `title` - Event title
- `description` - Event description
- `eventDate` - Event date
- `createdBy` - Event creator (teacher)
- `createdByName` - Cached creator name

### Queries Table
- `studentId` - Student who asked
- `question` - Question text
- `answer` - Teacher's answer (optional)
- `teacherId` - Teacher who answered (optional)
- `answeredAt` - Answer timestamp (optional)
- `studentName` - Cached student name
- `teacherName` - Cached teacher name

## 🔧 Key Features Implementation

### Real-time Updates
All data updates are automatically synchronized across all connected clients using Convex's reactive queries.

### File Upload System
Images are uploaded to Convex storage with automatic URL generation and secure access.

### Role-based Access Control
- Students can create posts, ask questions, and view events
- Teachers can create posts, events, and answer questions
- UI adapts based on user role

### Responsive Design
Fully responsive design that works on desktop, tablet, and mobile devices.

## 🚀 Deployment

### Deploy to Convex Cloud

1. **Build the project**
   ```bash
   npm run build
   ```

2. **Deploy to Convex**
   ```bash
   npx convex deploy
   ```

3. **Deploy frontend**
   Deploy the `dist` folder to your preferred hosting service (Vercel, Netlify, etc.)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Built with [Convex](https://convex.dev) for the backend
- UI components styled with [Tailwind CSS](https://tailwindcss.com)
- Icons and emojis for enhanced user experience

## 📞 Support

For support, email support@campusconnect.com or join our community Discord.

---

**Campus Connect** - Connecting students and teachers in the digital age! 🎓✨
